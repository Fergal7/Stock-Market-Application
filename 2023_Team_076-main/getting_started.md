# Getting Started

Welcome to the University of Groningen's Advanced Object Oriented Programming course! We're glad to have you. These instructions below serve as a short summary of how to work with Git & GitHub.

## Setting Up

To begin, you'll need to download this repository you're looking at right now. To do that, run the following command:

```
git clone <repository_url>
```
Here `<repository_url>` is the URL of your own repository; not the 20xx_Assignments repository.

This will download your repository to the current working directory, into a folder with the same name as the repository's name. This is all you need to do. When assignments are made available, you'll see them appear in your repository in a new branch with a name similar to the assignment name.

## Developing Your Code and Submitting

While previous knowledge of Git may be useful, it should not really be needed for you to do well in this course, as long as you know how to submit each assignment.

When you have finished working on your code, you should first add any new files which you have created (`-A` means "all files").

```sh
git add -A
```

Alternatively, you can add all files in the current working directory using:

```sh
git add .
```

Now, commit all changes that have been made:

```sh
git commit -m "This is a short message about what I did."
```

Make sure to give your commits a meaningful message! You can also skip the `git add` command by using the `-am` flag instead of `-m`.
To make your committed changes public, you need to upload them to the remote repository. This is done by 'pushing':

```sh
git push
```

If you refresh the page for your repository on github.com, you should now see the changes in whatever branch you pushed to.

### Submission

Once you've committed and pushed a few times, and you feel that your code is ready to be submitted, create a new pull request from the assignment branch onto the `main` branch. When you do this, a number of automated checks will run that will compile your code and run `mvn clean test` to see that all checks and tests (if any) pass. If your code does not compile or pass all tests, don't bother making a submission; we won't grade it. Fix it and submit a working version.

If you have made a pull request, but then would like to make some last-minute changes, you can do this by simply adding more commits to the branch in question. Any pushed commits will simply be added to an existing pull request. Always make sure that `mvn clean test` builds your code successfully and that a green checkmark appears next to your commit.

## Questions, Comments, Concerns

Should you have any questions about Github, the above workflow, or other technical questions, please first see if Google can help, and then if you still cannot figure out what to do, make an issue in your repository.
