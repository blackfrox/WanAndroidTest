package com.example.wanandroidtest.other.db


/**
 * Created by Administrator on 2018/4/20 0020.
 * PS:greenDao创建的表，会自动生成daoMaster文件,如果之前创建过然后删了的话，
 * 需要注意一下选择的DaoMaster的路径
 */

//问题 ： DaoSession老是报错，无法找到，真是日了狗了
//class GreenDaoHelper
////@Inject
//internal constructor() : DbHelper{
//
////    private val mDaoSession: DaoSession by lazy {
////        val helper= DaoMaster.DevOpenHelper(GeeksApp.instance,Constants.DB_NAME)
////        val dataBase=helper.writableDatabase
////        val daoMaster=DaoMaster(dataBase)
////        daoMaster.newSession()
////    }
//
//    init {
////        val helper=DaoMaster.DevOpenHelper(GeeksApp.instance,Constants.DB_NAME)
////        val dataBase=helper.writableDatabase
////        val daoMaster=DaoMaster(dataBase)
////        mDaoSession=daoMaster.newSession()
//    }
//    override fun addHistoryData(data: String): List<HistoryData> {
//        val historyDataDao=mDaoSession.historyDataDao
//        val mList =historyDataDao.loadAll()
//        val iterator = mList.iterator()
//        val item1= HistoryData(System.currentTimeMillis(),data)
//        //这里可以优化的，改成update，卧槽，为什么全删了，然后再重新插入
//        //如果data已存在,则更新该item的时间
//        while (iterator.hasNext()) {
//            val item2 = iterator.next()
//            if (item2.data==data){
//                mList.remove(item2)
//                mList.add(item1)
//                historyDataDao.deleteAll()
//                historyDataDao.insertInTx(mList)
//                return mList
//            }
//        }
//        //限定容量为10个数据
//        if (mList.size<10)
//            historyDataDao.insert(item1)
//        else{
//            mList.removeAt(0)
//            mList.add(item1)
//            historyDataDao.deleteAll()
//            historyDataDao.insertInTx(mList)
//        }
//        return mList
//    }
//
//    override fun clearHistoryData() {
//        mDaoSession.historyDataDao.deleteAll()
//    }
//
//    override fun loadAllHistoryData(): List<HistoryData> {
//        return mDaoSession.historyDataDao.loadAll()
//    }
//}
