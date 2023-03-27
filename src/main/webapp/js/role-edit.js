$(document).ready(function () {
    let roleId = 0
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/role-edit`,
    }).done(function (data) {
        if(data[0].data != null) {
            console.log(data.data)
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }
        if(data[1].data != null){
            roleId = data[1].data["id"]
            $('#input-name').attr('value',data[1].data["name"])
            $('#input-desc').attr('value',data[1].data["description"])
        } else {
            $('#page-tittle').html("Không tìm thấy dữ liệu quyền")
        }
    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/role-edit`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    $('#btn-edit-role').click(function (e){
        e.preventDefault()
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/role-edit`,
            data: {
                'function': 'editRole',
                'id': roleId,
                'name': $('#input-name').val(),
                'description': $('#input-desc').val()
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