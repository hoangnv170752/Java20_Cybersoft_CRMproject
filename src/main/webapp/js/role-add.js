$(document).ready(function () {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/role-add`,
    }).done(function (data) {
        if (data[0].data != null) {
            console.log(data.data)
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }
    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/role-add`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    $('#btn-add-role').click(function (e){
        e.preventDefault()
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/role-add`,
            data: {
                'function': 'addRole',
                'name': $('#input-name').val(),
                'description': $('#input-desc').val()
            }
        }).done(function (data){
            switch (data.data){
                case -1:
                    alert(data.message)
                    break
                case 1:
                    alert(data.message)
                    break
                case 0:
                    alert(data.message)
                    break
                default:
                    break
            }
        })
    })

})