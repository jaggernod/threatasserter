# Threat Asserter [![Build Status](https://travis-ci.org/jaggernod/threatasserter.svg?branch=master)](https://travis-ci.org/jaggernod/threatasserter)

Annotation-based thread assertion.

As a mobile developer, we do tend to be punished by performing long running tasks (>16ms) on a UI Thread.
The Android platform does not restrict us from doing so and with time it is easy to run into a junky UI.

We can assert on which thread are we operating but this does dilute the expressiveness of our code.

Now instead of adding checks inside our methods just add `@AssertWorkerThread` and in `debug` build type
the code will throw exception pointing at the culprit and explain the cause.

You can also assert Main Thread with`@AssertMainThread` as sometimes it is hard to see if you are really performing 
UI related work on a UI thread. 

```java
@AssertMainThread
public static void onClickWithWorkerThreadHelper(Context context) {
  Toast.makeText(context, "Will certainly be displayed on Ui Thread... or die trying.", Toast.LENGTH_LONG).show();
}
```

Works as well in a Kotlin project.

```kotlin
@AssertWorkerThread
fun crashIfNotWorkerThread() {
  // Looong task. But will crash before getting here.
  SystemClock.sleep(1000)
}
```

The assertion will only happen in debug builds and the annotation itself is never present in the
compiled class file for any build type. This means you can keep the annotation and check it into
source control. It has zero effect on non-debug builds.

```groovy
buildscript {
  repositories {
    mavenCentral()
    jcenter()
  }

  dependencies {
    classpath 'com.jaggernod:threatasserter-plugin:1.0.7'
  }
}

apply plugin: 'threatasserter'
```

Disable logging temporarily by adding the following:

```groovy
threatAsserter {
    enabled true
}
```

Local Development
-----------------

 * `install` - Install plugin, runtime, and annotations into local repo.
 * `connectedDebugAndroidTest` - Build and runs the connected Android Tests on a device. Normal `connectedCheck` won't succeed as it is the release build.

Credits
-------
Thanks to [Jake Wharton](https://github.com/JakeWharton) are required as his project [Hugo](https://github.com/JakeWharton/hugo) has inspired me (and provided a reference point) to play around with AOP and create this project. 

License
--------

    Copyright 2017 Pawel Polanski

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
