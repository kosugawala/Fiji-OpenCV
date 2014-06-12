# Fiji-OpenCV

[OpenCV](http://docs.opencv.org/index.html) bindings for Fiji.

[Pattern's OpenCV package](https://github.com/PatternConsulting/opencv) is added to this fiji-plugin project via maven.

You can extend this project to your own fiji plugin.

## Install

[Eclipse](http://eclipse.org):
_File&gt;Import...&gt;Existing Maven Project_

## Run

[Eclipse](http://eclipse.org):
Right click on your project and select Run As&gt;Maven build...

Set Goals to "exec:java" and Run.

The result in Fiji Log would look like this:

```
Welcome to OpenCV 2.4.9.0
OpenCV Mat: Mat [ 5*10*CV_8UC1, isCont=true, isSubmat=false, nativeObj=0x7f01e4008810, dataAddr=0x7f01e40068f0 ]
OpenCV Mat data:
[0, 0, 0, 0, 0, 5, 0, 0, 0, 0;
  1, 1, 1, 1, 1, 5, 1, 1, 1, 1;
  0, 0, 0, 0, 0, 5, 0, 0, 0, 0;
  0, 0, 0, 0, 0, 5, 0, 0, 0, 0;
  0, 0, 0, 0, 0, 5, 0, 0, 0, 0]
```