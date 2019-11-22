$.fn.extend({
    verifyCode:function (options) {
        var defaultOptions={
            url:"/kaptcha/defaultKaptcha"
        }
        options=$.extend(defaultOptions,options)
        $(this).each(function (i,item) {
            $(item).click(function () {
                $(this).attr("src",options.url+"?ts="+new Date().getTime())
            })
        })
    },
    sessionUser:function(options){
        var defaultOptions={
            url:"/app/userApp/sessionUser"
        }
        options=$.extend(defaultOptions,options)
        $(this).each(function (i,item) {
            $.get(options.url,function(data){
              $(item).text(data.name)
            })
        })
    }
})