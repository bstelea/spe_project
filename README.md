[![CircleCI](https://circleci.com/gh/bstelea/spe_project.svg?style=svg)](https://circleci.com/gh/bstelea/spe_project) [![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fbstelea%2Fspe_project.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fbstelea%2Fspe_project?ref=badge_shield)
# Global Beer Shop Website
## Running the project
### Getting the required files
In order to run the server locally, first clone the repository on your machine with 
`git clone https://github.com/bstelea/spe_project.git`.
Next, you need to ask the owners for permission to obtain `config.properties` and place this file in the `spe_project` directory and for `secret.properties` which needs to be placed in the `spe_project/src/main/resources` directory.
### Running the server
After aquiring all the necessary files, run the server with `mvn spring-boot:run`.
## Running the tests
To run the tests of the project, run `mvn test`
