# Setup

Your `~/.aws/config` needs to specify the region, probably `us-east-1`, or some such.

Your `~/.aws/credentials` file has to have your access key and secret key specified.

OR

Put your credentials in `credentials.edn` (and make sure it's ignored by git),
and your config in `config.edn`.

Once you've got your credentials sorted, take a look at the
`software.justenough.aws-demo.orchestrator` namespace to see a couple examples
of creating and interacting with AWS services.
