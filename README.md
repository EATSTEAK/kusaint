# kusaint
Soongsil University(SSU) u-Saint Parser with [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html).

# Prerequisites

## JVM !!IMPORTANT!!
To run kusaint as a library in JVM environment. You have to install DigiCert root CAs from [u-Saint](https://saint.ssu.ac.kr) or other ssu sites.

This library won't disable ssl checks just because root CAs in ssu aren't included default keystore of JVM.

Download all three certificates, and install to `keystore` using `keytool`.

```shell
keytool -import -alias <alias> -cacerts -file <cert>
```

Write `alias` whatever you want, but add all three cert's path in `cert`.

After execution, you will prompt to type password of keystore.

Default password of keystore is `changeit`, so if you weren't changed the password, type it.

## JS
In JavaScript, kusaint automatically installs required certificates.

# Installation

## JVM
Add GitHub Packages repository with url `https://maven.pkg.github.com/EATSTEAK` and add the `xyz.eatsteak.kusaint-jvm` dependency.


### Maven
```xml
<dependencies>
    <dependency>
        <groupId>xyz.eatsteak</groupId>
        <artifactId>kusaint-jvm</artifactId>
        <version>1.0.1</version>
    </dependency>
    <!-- Other dependencies... -->
</dependencies>
```

### Gradle (Groovy)
```groovy
dependencies {
    implementation 'xyz.eatsteak:kusaint-jvm:1.0.1'
    // Other dependencies...
}
```

### Gradle (Kotlin DSL)
```kotlin
dependencies {
    implementation("xyz.eatsteak:kusaint-jvm:1.0.1")
}
```

## JS
JavaScript Installation isn't ready for npm yet.

If you use `gradle` or `maven` for javascript dependencies, you can add dependency `xyz.eatsteak.kusaint-js`.

# Usage
For more info, please refer to [Docs](https://eatsteak.github.io/kusaint).

## Examples

### Kotlin
```kotlin
val kusaint = Kusaint()

kusaint.timeTable.major.find(2021, "2 학기", "IT대학", "글로벌미디어학부") // Find all lectures with given major.
kusaint.timeTable.requiredElectives.find(2021, "2 학기", "컴퓨팅적사고") // Find all required elective lectures with given lecture name.
```