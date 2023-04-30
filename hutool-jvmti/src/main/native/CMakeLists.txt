cmake_minimum_required(VERSION 3.17)
project(hutool-jvmti)

set(CMAKE_CXX_STANDARD 11)

add_library(jni-lib SHARED src/jni-library.cpp)

include_directories(head)
IF (WIN32)
    include_directories(head/windows)
ELSEIF (APPLE)
    include_directories(head/macos)
ELSEIF (common/linux)
    include_directories(head/linux)
ENDIF ()
