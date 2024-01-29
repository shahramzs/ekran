package com.example.ekran.services.http

class Api {

    companion object{
        const val ROOT_URL:String = "http://192.168.1.2:8000/"
        const val ROOT_URL2 = "http://192.168.1.2:8000"
        private const val SOCKET = "ws://192.168.1.2:8000/"

        const val GET_VIDEO = "getVideo/"
        const val GET_Category = "category/"
        const val GET_BANNER = "banner/"
        const val GET_SUBSCRIBER = "get_subscriber/"
        const val GET_COMMENT_NUMBER = "get_comment_number/"
        const val GET_VIDEO_SAME = "get_video_same/"
        const val GET_VIDEO_SIMILAR ="get_video_similar/"
        const val UPLOAD_VIDEO = "uploadVideo/"
        const val UPLOAD_VIDEO_DETAILS = "uploadVideoDetail/"
        const val CONVERT_VIDEO = "convert_video/"
        const val SOCKET_URL = SOCKET + "progress_url/"
        const val REGISTER = "register/"
        const val AUTH = "auth/"
        const val SUBSCRIBE = "subscribe/"
        const val LIKE = "likeVideo/"
        const val DISLIKE = "dislikeVideo/"
        const val GET_LIKE_NUMBER = "get_like/"
        const val GET_DISLIKE_NUMBER = "get_dislike/"
        const val SAVE = "saveVideo/"
        const val ADD_COMMENT = "addComment/"
        const val GET_COMMENT = "get_comment/"
        const val GET_SUBSCRIBER_BY_USER = "get_subscribers_by_user/"
        const val DELETE_SUBSCRIBER_BY_USER = "delete_subscriber_by_user/"
        const val GET_SAVE_BY_USER = "get_saveVideo_by_user/"
        const val DELETE_SAVE_BY_USER = "delete_saveVideo_by_user/"
    }
}