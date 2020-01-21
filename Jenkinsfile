@Library(['epo-pipeline-library@next', 'fis-global-utils']) _
import io.fis.*

// =====================================================================
// Copyright 1999-2020 Fidelity National Information Services, Inc.
// and/or its subsidiaries - All Rights Reserved worldwide.
//
// This document is protected under the trade secret and copyright laws
// as the property of Fidelity National Information Services, Inc.
// and/or its subsidiaries.
//
// Copying, reproduction or distribution should be limited and only to
// employees with a "need to know" to do their job. Any disclosure
// of this document to third parties is strictly prohibited.
// =====================================================================

//////////////////////////////////////////////////////
// These should be the only two lines needing changes between IMPLs
def imageName = 'docker-epo-local.docker.fisdev.local/epo/fis/api/spi-{{API_NAME}}-{{CORE}}'
def chartName = 'fis-api-spi-{{API_NAME}}-{{CORE}}'

//////////////////////////////////////////////////////
def cloudId = 'epo-cicd'
def cicdNamespace = 'epo-cicd'
def teamsWebHookCode = '4eaaa4f4-b8dd-4634-a517-748acc3c043f@e3ff91d8-34c8-4b15-a0b4-18910a6ac575/IncomingWebhook/ef621719db2a4c47a48ffe550c610143/6fffabed-aede-4005-8b4f-f03b54084d2d'
def status = JobStatus.Successful

def buildImage = openshiftImageBuilder(
  pushSecret: 'epo-artifactory',
  pullSecret: 'artifactory'
)

def artifactInfo = ''
def pomVersion = ''

def chartImpl = '-dev-impl'
def chartVersion = ''

def deployNamespace = 'epo-fisapi-dev'
def releaseName = ''
def tag = ''
def agentImages = []

switch (env.BRANCH_NAME) {
  case "sprint":
    deployNamespace = 'epo-fisapi-sprint'
    releaseName='sprint'
    tag = "-${releaseName}"
    agentImages = [epo.standardAgents().maven,
                   epo.standardAgents().helm,
                   epo.standardAgents().oc,
                   epo.standardAgents().sonar,
                   epo.standardAgents().veracode
                  ]
    break
  case "develop":
    deployNamespace = 'epo-fisapi-dev'
    releaseName='dev'
    tag = "-${releaseName}"
    agentImages = [epo.standardAgents().maven,
                   epo.standardAgents().helm,
                   epo.standardAgents().oc,
                   epo.standardAgents().sonar,
                   epo.standardAgents().veracode,
                   epo.standardAgents().blackduck
                  ]
    break
  default:
    echo "default branch type - No helm chart processing [${env.BRANCH_NAME}]"
    agentImages = [epo.standardAgents().maven,
                   epo.standardAgents().sonar
                  ]
}

def buildAgent = agentBuilder(
  cloud: cloudId,
  namespace: cicdNamespace,
  agents: agentImages
)

//////////////////////////////////////////////////////
def stages = [
  build: {
    container('maven') {
      sh 'mvn clean package'
    }
  },

  buildImage: {
    container('oc') {
      buildImage(
        name: imageName,
        tag: chartVersion,
        contextDir: './deployments/server',
        dockerfilePath: 'Dockerfile',
        buildArgs: [
          'pomVersion' : pomVersion
        ]

      )
    }
  },

  publishChart: {
    container('helm') {
      artifactInfo = mavenArtifactInfo()
      echo "artifactInfo.key=${artifactInfo.key}    artifactInfo.name=${artifactInfo.name}    artifactInfo.version=${artifactInfo.version}"

      pomVersion = artifactInfo.version
      def imageVersion = artifactInfo.version.replaceAll("SNAPSHOT", "latest")

      chartVersion = "${imageVersion}${tag}"

      echo "pomVersion=${pomVersion}  imageVersion=${imageVersion}  chartVersion=${chartVersion}  chartName=${chartName}  chartImpl=${chartImpl}  tag=${tag}"

      yamlUpdate("charts/${chartName}/Chart.yaml") { chart ->
        chart.version = "${chartVersion}"
      }

      yamlUpdate("charts/${chartName}${chartImpl}/Chart.yaml") { chart ->
        chart.version = "${chartVersion}"
      }

      yamlUpdate("charts/${chartName}${chartImpl}/requirements.yaml") { data ->
        data.dependencies.each { item ->
          if (item.name == "${chartName}") {
            item.version = "${chartVersion}"
          }
        }
      }

      yamlUpdate("charts/${chartName}${chartImpl}/values.yaml") { values ->
        values."${chartName}".deployment.image.tag = "${pomVersion}"
      }

      dir('charts') {
        sh """
          helm repo update
          helm dependency update $chartName
          helm_push $chartName helm-epo-local
          helm dependency update $chartName$chartImpl
          helm_push $chartName$chartImpl helm-epo-local
        """
      }
    }
  },

  publish: {
    container('maven') {
      sh 'mvn clean deploy'
    }
  },

  quality: {
    container('sonar') {
      sonarScan()
    }

    def exclusions = ['**/charts/**/*', '**/specs/**/*', '**/tests/**/*','**/integration-tests/**/*']

    // Only run Veracode for every branch but feature
    switch ("${env.BRANCH_NAME}") {
      case ~/^feature\/.*/:
        break
      default:
        artifactInfo = mavenArtifactInfo()
        echo "artifactInfo.key=${artifactInfo.key}    artifactInfo.name=${artifactInfo.name}    artifactInfo.version=${artifactInfo.version}"

        container('veracode') {
          def reportingBranch = ''
          def sandboxName = ''

          switch ("${env.BRANCH_NAME}") {
            case ~/^PR-.*/:
              sandboxName = "${artifactInfo.key}:PR"
              if (env?.CHANGE_TARGET) {
                reportingBranch = "${env.CHANGE_TARGET}-"
              }
              break
            default:
              sandboxName = "${artifactInfo.key}:${env.BRANCH_NAME}"
              break
          }

          veracodeScan(
            appName: "Digital One",
            sandboxName: "${sandboxName}",
            scanName: "${artifactInfo.version}-${reportingBranch}${buildId()}",
            exclusions: exclusions
          )
        }
      break
    }

    if ((env.BRANCH_NAME == 'develop') ||
        (env.BRANCH_NAME == 'master'))	{
      container('blackduck') {
        blackduckScan(
          assetId: "14956",
          exclusions: exclusions
        )
      }
    }
  },

  deployImage: {
    container('helm') {
      // This line is for trouble-shooting. To use it, copy/paste it as the next line in the "sh..." section here
	  // helm delete --purge $chartName-$releaseName --tiller-namespace $cicdNamespace

      sh """
        helm repo update
        helm upgrade $chartName-$releaseName 'helm-epo-virtual/$chartName$chartImpl' --set ${chartName}.deployment.image.tag='$chartVersion' --recreate-pods --tiller-namespace $cicdNamespace --namespace $deployNamespace --version ${chartVersion} --install --force --wait
      """
    }
  }
]

def prePipeline = {
  try {
    node("docker") {
      stage('notify') {
        notifyMicrosoftTeams(teamsWebHookCode) {
          message = "Commits:<br>&emsp;&emsp;${changes}<br>"
        }
      }
    }
  }
  catch (e) {
    echo 'Skip MS Teams exception - not important'
  }
}

def postPipeline = {
  if (currentBuild?.result) { status = JobStatus.Failed }
  try {
    notifyMicrosoftTeams(teamsWebHookCode) {
      buildStatus = status
      message = 'Done'
    }
  }
  catch (e) {
    echo "Post ${status} - Skip MS Teams exception - not important"
  }
}

//////////////////////////////////////////////////////
timestamps {
  buildAgent {
    epo.standardPipelines(stages) { pipelines ->
      if (env.BRANCH_NAME == 'develop') {
        pipelines.prePipeline = prePipeline
        pipelines.postPipeline = postPipeline
      }

      pipelines.feature = {
        epo.runStages(stages, ['build', 'quality'])
      }

      pipelines.custom = [
        'sprint': {
          try {
            prePipeline()
            epo.runStages(stages, ['build', 'quality', 'publishChart', 'buildImage', 'deployImage'])
          }
          catch (e) {
            currentBuild.result = "FAILED"
            throw (e)
          }
          finally {
            node("docker") {
              postPipeline()
            }
          }
        }
      ]

      pipelines.develop = {
        epo.runStages(stages, ['build', 'quality', 'publish', 'publishChart', 'buildImage', 'deployImage'])
      }

      pipelines.release = {
        epo.runStages(stages, ['build'])
      }

      pipelines.master = {
        epo.runStages(stages, ['quality', 'publish'])
      }
    }
  }
}
