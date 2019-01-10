#include <jni.h>
#include <string>
#include <regex>
#include <algorithm>
#include <sstream>



std::string encode(std::string& data) {
    std::string buffer;
    buffer.reserve(data.size());
    for(size_t pos = 0; pos != data.size(); ++pos) {
        switch(data[pos]) {

            case '\"': buffer.append("\\\"");      break;
            case '\'': buffer.append("\\'");      break;

            default:   buffer.append(&data[pos], 1); break;
        }
    }
    return buffer;
}




extern "C" JNIEXPORT jstring JNICALL

Java_mawso3a_noon_mix_MainActivity_stringFromJNI(
        JNIEnv *env,
jobject /* this */,
jstring html) {

std::string s = env->GetStringUTFChars(html, NULL);

// std::replace( html.begin(), html.end(), 'ddvdbfdb', 'ddvdbfdb');

return env->NewStringUTF(s.c_str());
}


extern "C" JNIEXPORT jstring JNICALL

Java_mawso3a_noon_mix_CrossActivity_stringFromJNICross(
        JNIEnv *env,
        jobject /* this */,
        jstring html) {

    std::string s = env->GetStringUTFChars(html, NULL);


    //s = encode(s);
    s = "this is text come from c++ ,i want to provide pcrecpp functions like preg_replace ,preg_split,strip_html with exception tags, using pcrecpp library to process the html String come form java";




    return env->NewStringUTF(s.c_str());
}

extern "C" JNIEXPORT jstring JNICALL

Java_mawso3a_noon_mix_NocrossActivity_stringFromJNInoCross(
        JNIEnv *env,
        jobject /* this */,
        jstring html) {

    std::string s = env->GetStringUTFChars(html, NULL);

    //s = encode(s);
      s = "this is text come from c++ ,i want to provide pcrecpp functions like preg_replace ,preg_split,strip_html with exception tags, using pcrecpp library to process the html String come form java";


    return env->NewStringUTF(s.c_str());
}



