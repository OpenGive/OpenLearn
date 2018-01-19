# Open Learn :books:

OpenLearn is an open-source secure learning management system designed for
simplicity and easy use. It was born out of the [OpenGive 2017
hackathon](https://opengive.credera.com/) where hackers gather to build cool
projects for non-profits.

## Why?

There are many charitable, education-focused non-profits that are in need of a
system to help organize and facilitate their courses, programs, and assignments.
OpenLearn was created to answer that need.

This application provides those non-profits with a centralized, web-based
application to securely store and manage educational data, create courses and
assignments, and provide a place to manage student grades and submissions.

## Feature areas

There are 3 major feature areas that OpenLearn is built around:

1. Organizing and centralizing educational data (courses, programs, etc.)
2. Facilitating courses and assignments
3. Providing course and assignment information for students

Overarching above all these features is a strong security requirement.

A complete list of features is "in-the-works" on our
[wiki](https://github.com/OpenGive/OpenLearn/wiki/Feature-List)

## Contributing

Checkout the [issues page](https://github.com/OpenGive/OpenLearn/issues) for
places to start helping out! We are working on documenting issues that need help
by marking them with the *help wanted* or *good first issue* labels.

### Technologies

- Angular
- Java Spring Boot
- MariaDB
- AWS
  - S3
  - KMS (optional)
- ReCaptcha

OpenLearn is built as a Angular [Single Page Application
(SPA)](https://en.wikipedia.org/wiki/Single-page_application) written in
TypeScript backed by a REST API built with Java Spring Boot using Java 8.
The database used to store a majority of the data in OpenLearn is
[MariaDB](https://mariadb.org/).

Amazon Web Services (AWS) *S3* storage service
is used to store file information (like assignment uploads). By default, these
files are encrypted, but an optional custom AWS encryption key made using AWS's
Key Management Service (KMS) can be used to encrypt those uploaded files.

Finally, ReCaptcha is configured to be used with the application as an
additional layer of security.

All things SPA live under the `www` directory. The REST API lives under the
`services` directory.

There is a step-by-step setup for development walk-through be crafted in the
[wiki](https://github.com/OpenGive/OpenLearn/wiki/Setup-Development-Environment-(step-by-step))

## How can I setup OpenLearn for my organization?

Are you a non-profit interested in setting up OpenLearn for your organization?
:smile:

Awesome! We're happy to help you. Eventually, there will be "easy self
deployment" guide you can use with recommendations and more, but for now we
recommend you reach out to us at OpenGive directly. We'll be happy to help you
get setup.
