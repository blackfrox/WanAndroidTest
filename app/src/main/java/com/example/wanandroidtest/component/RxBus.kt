package com.example.wanandroidtest.component

import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import io.reactivex.subscribers.SerializedSubscriber

/**
 *
 * 代替广播 进行组件之间的通信
 * 上级注册接收toFlowable，下级发送post
 */

class RxBus {

    //调用toSerialized(),保证线程安全
    private val mBus=PublishProcessor.create<Any>().toSerialized()

    /**
     * 发送消息
     */
    fun post(o: Any)=SerializedSubscriber(mBus).onNext(o)

    /**
     * 确定接收消息的类型
     */
    fun <T>toFlowable(aClass: Class<T>): Flowable<T> {
        return mBus.ofType(aClass)
    }

    /**
     * 判断是否有订阅
     */
    fun hasSubscribers()=
            mBus.hasSubscribers()

    companion object {
        val instance by lazy { RxBus() }
        fun getDefault() = instance
    }

}
