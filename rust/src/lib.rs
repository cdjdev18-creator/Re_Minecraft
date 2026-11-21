use jni::JNIEnv;
use jni::objects::JClass;
use jni::sys::jint;

#[no_mangle]
pub extern "system" fn Java_com_reminecraft_NativeBridge_rustVersion(_env: JNIEnv, _class: JClass) -> jint {
    1
}
