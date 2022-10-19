#!groovy
@Library('jenkins-pipeline') import com.github.jcustenborder.jenkins.pipeline.MavenGitHubReleasePipeline

def pipe = new MavenGitHubReleasePipeline()
pipe.artifacts = "operator/target/helm/repo/connect-k8s-*.tgz"
pipe.execute()