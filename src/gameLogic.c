#include <stdio.h>
#include <string.h>
#include "GameLogicJNI.h"

JNIEXPORT void JNICALL Java_GameLogicJNI_addDoDPlayer(JNIEnv *, jobject, jint);

JNIEXPORT jintArray JNICALL Java_GameLogicJNI_getSpawnLocation(JNIEnv *, jobject);

JNIEXPORT jstring JNICALL Java_GameLogicJNI_processCommand(JNIEnv *, jobject, jstring, jint);

JNIEXPORT jboolean JNICALL Java_GameLogicJNI_gameRunning(JNIEnv *env, jobject thisObj) {
	//get map class
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);
	
	//get map field
	jfieldID activeFieldID = (*env)->GetFieldID(env, thisClass, "active", "Z");
	
	//get map object
	jboolean activeValue = (*env)->GetBooleanField(env, thisObj, activeFieldID);
	
	return activeValue;
}

JNIEXPORT jstring JNICALL Java_GameLogicJNI_hello(JNIEnv *env, jobject thisObj, jobject player) {
	//get game logic jni class
	jclass gameLogicJNIClass = (*env)->GetObjectClass(env, thisObj);
	
	//get map field id
	jfieldID mapFieldID = (*env)->GetFieldID(env, gameLogicJNIClass, "map", "LMapJNI;");
	
	//get map object
	jobject mapObject = (*env)->GetObjectField(env, thisObj, mapFieldID);
	
	//get player and map classes
	jclass playerClass = (*env)->GetObjectClass(env, player);
	jclass mapClass = (*env)->GetObjectClass(env, mapObject);
	
	//get getCollectedGold method id
	jmethodID getCollectedGoldMethodID = (*env)->GetMethodID(env, playerClass, "getCollectedGold", "()I");
	if (getCollectedGoldMethodID == 0) return NULL;
	
	//get getGoldToWin method id
	jmethodID getGoldToWinMethodID = (*env)->GetMethodID(env, mapClass, "getGoldToWin", "()I");
	if (getGoldToWinMethodID == 0) return NULL;
	
	//get collected gold from player
	jint collectedGold = (*env)->CallIntMethod(env, player, getCollectedGoldMethodID);
	
	//get gold to win from map
	jint goldToWin = (*env)->CallIntMethod(env, mapObject, getGoldToWinMethodID);
	
	int goldRequired;
	if (collectedGold > goldToWin)
		goldRequired = 0;
	else
		goldRequired = goldToWin - collectedGold;
	
	int lengthOfGoldRequired = goldRequired / 10;
	
	char returnString[7 + lengthOfGoldRequired];
	
	sprintf(returnString, "GOLD: %d", goldRequired);
	
	return (*env)->NewStringUTF(env, returnString);
}

JNIEXPORT jstring JNICALL Java_GameLogicJNI_move(JNIEnv *, jobject, jobject, jchar);

JNIEXPORT jboolean JNICALL Java_GameLogicJNI_isAnotherPlayerOccupyingTile(JNIEnv *, jobject, jint, jint);

JNIEXPORT jstring JNICALL Java_GameLogicJNI_look(JNIEnv *, jobject, jobject);

JNIEXPORT jobjectArray JNICALL Java_GameLogicJNI_getVisibleOpponents(JNIEnv *, jobject, jobjectArray, jobject);

JNIEXPORT jstring JNICALL Java_GameLogicJNI_pickup(JNIEnv *, jobject, jobject);

JNIEXPORT jboolean JNICALL Java_GameLogicJNI_checkWin(JNIEnv *, jobject, jobject);
  
JNIEXPORT void JNICALL Java_GameLogicJNI_quitGame(JNIEnv *, jobject, jobject);
