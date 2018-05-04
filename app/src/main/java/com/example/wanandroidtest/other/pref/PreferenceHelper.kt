package com.example.wanandroidtest.other.pref

/**
 * @author quchao
 * @date 2017/11/27
 */

interface PreferenceHelper {

    /**
     * Get login account
     *
     * @return account
     */
    /**
     * Set login account
     *
     * @param account Account
     */
    var loginAccount: String

    /**
     * Get login password
     *
     * @return password
     */
    /**
     * Set login password
     *
     * @param password Password
     */
    var loginPassword: String

    /**
     * Get login status
     *
     * @return login status
     */
    /**
     * Set login status
     *
     * @param isLogin IsLogin
     */
    var loginStatus: Boolean

    /**
     * Get current page
     *
     * @return current page
     */
    /**
     * Set current page
     *
     * @param position Position
     */
    var currentPage: Int

    /**
     * Get project current page
     *
     * @return current page
     */
    /**
     * Set project current page
     *
     * @param position Position
     */
    var projectCurrentPage: Int

    /**
     * Get auto cache state
     *
     * @return if auto cache state
     */
    /**
     * Set auto cache state
     *
     * @param b current auto cache state
     */
    var autoCacheState: Boolean

    /**
     * Get no image state
     *
     * @return if has image state
     */
    /**
     * Set no image state
     *
     * @param b current no image state
     */
    var imageState: Boolean

    /**
     * Get night mode state
     *
     * @return if is night mode
     */
    /**
     * Set night mode state
     *
     * @param b current night mode state
     */
    var nightModeState: Boolean

}
