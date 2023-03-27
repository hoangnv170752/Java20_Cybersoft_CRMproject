$(document).ready(function () {
    let userId = 0
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/user-edit`,
    }).done(function (data) {
        if (data[0].data != null) {
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }
        if(data[1].data != null){
            let userInfo = data[1].data
            userId = userInfo["id"]
            $('#input-fullname').attr('value',userInfo["fullname"])
            $('#input-email').attr('value',userInfo["email"])
            $('#input-password').attr('value',userInfo["password"])
            $('#input-confirm-password').attr('value',userInfo["password"])
            $('#input-avatar').attr('value',userInfo["avatar"])
            if(data[2].data != null){
                let roleList = data[2].data
                for (const i in roleList){
                    const html = `<option value="${roleList[i]["id"]}">${roleList[i]["name"]}</option>`
                    $('#select-role').append(html)
                }
                document.getElementById('select-role').value = userInfo["role"]["id"]
            }
        } else{
            $('#tittle-content').html('Không tìm thấy dữ liệu thành viên')
        }
    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/user-edit`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    $('#btn-edit-user').click(function (e){
        e.preventDefault()
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/user-edit`,
            data: {
                'function': 'editUser',
                'id': userId,
                'fullname': $('#input-fullname').val(),
                'email': $('#input-email').val(),
                'password': $('#input-password').val(),
                'confirm-password': $('#input-confirm-password').val(),
                'avatar': $('#input-avatar').val(),
                'role-id': document.getElementById('select-role').value
            }
        }).done(function (data){
            switch (data.data){
                case -3:
                    alert(data.message)
                    break
                case -2:
                    alert(data.message)
                    break
                case -1:
                    alert(data.message)
                    break
                case 0:
                    alert(data.message)
                    break
                case 1:
                    alert(data.message)
                    break
                default:
                    break
            }
        })
    })





})