#include <stdio.h>
#include "GameLogicJNI.h"

JNIEXPORT void JNICALL Java_GameLogicJNI_addDoDPlayer(JNIEnv *, jobject, jint);

JNIEXPORT jintArray JNICALL Java_GameLogicJNI_getSpawnLocation(JNIEnv *, jobject);

JNIEXPORT jstring JNICALL Java_GameLogicJNI_processCommand(JNIEnv *, jobject, jstring, jint);

JNIEXPORT jboolean JNICALL Java_GameLogicJNI_gameRunning(JNIEnv *, jobject);

JNIEXPORT jstring JNICALL Java_GameLogicJNI_hello(JNIEnv *, jobject, jobject);

JNIEXPORT jstring JNICALL Java_GameLogicJNI_move(JNIEnv *, jobject, jobject, jchar);

JNIEXPORT jboolean JNICALL Java_GameLogicJNI_isAnotherPlayerOccupyingTile(JNIEnv *, jobject, jint, jint);

JNIEXPORT jstring JNICALL Java_GameLogicJNI_look(JNIEnv *, jobject, jobject);

JNIEXPORT jobjectArray JNICALL Java_GameLogicJNI_getVisibleOpponents(JNIEnv *, jobject, jobjectArray, jobject);

JNIEXPORT jstring JNICALL Java_GameLogicJNI_pickup(JNIEnv *, jobject, jobject);

JNIEXPORT jboolean JNICALL Java_GameLogicJNI_checkWin(JNIEnv *, jobject, jobject);
  
JNIEXPORT void JNICALL Java_GameLogicJNI_quitGame(JNIEnv *, jobject, jobject);
