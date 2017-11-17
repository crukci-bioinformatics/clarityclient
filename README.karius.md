# Notes

* Updated Java from 1.6 to 1.8 in `pom.xml`
* Created `~/.m2/toolchains.xml` as follows:
```
<toolchains>
 <toolchain>
  <type>jdk</type>
  <provides>
   <vendor>sun</vendor>
   <version>1.8</version>
  </provides>
  <configuration>
   <jdkHome>/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home</jdkHome>
  </configuration>
 </toolchain>
</toolchains>
```
* Proper `.gitignore`
