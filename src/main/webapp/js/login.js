$(document).ready(function (){
    $("#btn-login").click(function (){
        const email = $('#input-email').val()
        const pass = $('#input-password').val()

        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/login`,
            data:{
                'email': email,
                'password': pass
            }
        }).done(function (data){
            if(data.data){
                window.location.href = "/home"
            } else{
                $("#text-announce").html('Email không tồn tại hoặc mật khẩu không đúng')
            }
        })
    })
})