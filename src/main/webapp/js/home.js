$(document).ready(function (){
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/home`,
    }).done(function (data){
        if(data[0].data != null){
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar').attr('src','plugins/images/users/'+data[0].data["avatar"])
        }
        if(data[1].data != null){
            $('#task-unbegun').html(data[1].data[1])
            $('#task-doing').html(data[1].data[2])
            $('#task-finish').html(data[1].data[3])
            $('#progress-bar-unbegun').attr('style','width: '+(data[1].data[1]*100/data[1].data[0])+'%')
            $('#progress-bar-doing').attr('style','width: '+(data[1].data[2]*100/data[1].data[0])+'%')
            $('#progress-bar-finish').attr('style','width: '+(data[1].data[3]*100/data[1].data[0])+'%')
        }
    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/home`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

})