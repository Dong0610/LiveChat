package dong.duan.livechat.utility

open class Event<out T>(val content:T) {
    var hasbeenHandeler= false
    fun getContentOrNull(): T?{
        return  if(hasbeenHandeler) null
        else{
            hasbeenHandeler=true
            content
        }
    }

}