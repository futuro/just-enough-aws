# Must Haves

Your `~/.aws/config` needs to specify the region, probably `us-east-1`, or some such.

Your `~/.aws/credentials` file has to have your access key and secret key specified.

OR

Put your credentials in `credentials.edn` (and make sure it's ignored by git),
and your config in `config.edn`.

The credentials should be in the form
```edn
{:some-name
 {:access-id "AKIA...."
  :secret-key "******"}}
```

This allows you to have multiple credentials, though perhaps that's over engineering...

# TODO

[ ] Is it worth explaining how to pass this in as data, or just keep it in
    those files and tell people to figure it out?
0. How to use REBL to explore the docs, as they're part of the library.
1. How to make a bucket
2. How to set the ACL on that bucket to most restrictive
3. How to upload a object (slurp a page/image from the web and convert to byte array)
4. How to make that object world-readable
5. How to show the url for that object

* Make a function that takes an operation keyword and returns its slurped
  documentation website contents. This is so the REBL can display them in the
  view pane. Maybe build that during the demo?
* One difference between `aws/doc` and just reading the doc string in REBL is
  that the doc command will print out the request body, required keys, and the
  return body type.

