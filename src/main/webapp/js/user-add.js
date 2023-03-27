$(document).ready(function () {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/user-add`,
    }).done(function (data) {
        if (data[0].data != null) {
            console.log(data.data)
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }
        if(data[1].data != null){
            for (const i in data[1].data){
                const html = `<option value="${data[1].data[i]["id"]}">${data[1].data[i]["name"]}</option>`
                $('#select-role').append(html)
            }
        } else{
            $('#select-role').append(`<option>N/A</option>`)
        }
    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/user-add`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    $('#btn-add-user').click(function (e){
        e.preventDefault()
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/user-add`,
            data: {
                'function': 'addUser',
                'fullname': $('#input-fullname').val(),
                'email': $('#input-email').val(),
                'password': $('#input-password').val(),
                'confirm-password': $('#input-confirm-password').val(),
                'avatar': $('#input-avatar').val(),
                'role-id': document.getElementById('select-role').value
            }
        }).done(function (data){
            switch (data.data){
                case -2:
                    alert(data.message)
                    break
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