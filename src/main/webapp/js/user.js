let userData
$(document).ready(function (){
    const thisList = $('#user-list')
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/user`,
    }).done(function (data){
        if(data[0].data != null){
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src','plugins/images/users/'+data[0].data["avatar"])
        }
        if(data[1].data != null){
            userData = data[1].data
            for (const i in userData){
                let stt = Number(Number(i)+1)
                const html =   `<tr>
                                    <td id="stt">${stt}</td>
                                    <td>${data[1].data[i]["fullname"]}</td>
                                    <td id="name-email" >${data[1].data[i]["email"]}</td>
                                    <td>${data[1].data[i]["role"]["name"]}</td>
                                    <td>
                                        <a id="${data[1].data[i]["id"]}" href="#" class="btn btn-sm btn-primary btn-edit">Sửa</a>
                                        <a id="${data[1].data[i]["id"]}" href="#" class="btn btn-sm btn-danger btn-delete">Xóa</a>
                                        <a id="${data[1].data[i]["id"]}" href="#" class="btn btn-sm btn-info btn-detail">Xem</a>
                                    </td>
                                </tr>`
                //$('#example').find('tbody').append(html)
                $('#user-list').append(html)
            }
            $('#example').DataTable()
        } else {
            $('#example').DataTable()
        }

    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/user`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    $('#add-user').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/user`,
            data: {
                'function': 'goToAddUser'
            }
        }).done(function (data){
            if(data.data != null){
                window.location.href = data.data
            } else {
                alert('Bạn không có quyền truy cập trang này')
            }
        })
    })

    thisList.on('click','.btn-delete',function (e){
        e.preventDefault()
        const This = $(this) //id="name-email"
        const email = This.closest('tr').find("td[id='name-email']").html()
        let ok = confirm("Bạn có muốn xóa thành viên có Email: "+email+" này không?")
        if(ok){
            $.ajax({
                method: 'POST',
                url: `http://localhost:8080/api/user`,
                data: {
                    'function': 'deleteUser',
                    'memberID': $(this).attr('id')
                }
            }).done(function (data){
                if(data.data == 1){
                    let index = parseInt(This.closest('tr').find("td[id='stt']").html())-1
                    userData.splice(index,1)
                    $('#example').DataTable().clear().draw()
                    Reload()
                    alert(data.message)
                } else if(data.data == 0){
                    alert(data.message)
                } else if(data.data == -1){
                    alert('Thành viên này đang có công việc phụ trách. Vui lòng chuyển công việc sang người phụ trách khác hoặc xóa công việc trước khi xóa thành viên')
                } else if(data.data == -2){
                    alert('Thành viên này đang là quản lý của một dự án. Vui lòng chuyển quản lý của dự án hoặc xóa dự án trước khi xóa thành viên')
                } else if(data.data == 403){
                    alert('Bạn không có quyền xóa thành viên')
                }
            })
        }
    })

    thisList.on('click','.btn-edit',function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/user`,
            data: {
                'function': 'goToEditUser',
                'memberID': $(this).attr('id')
            }
        }).done(function (data){
            if(data.data != null){
                window.location.href = data.data;
            }
        })
    })

    thisList.on('click','.btn-detail',function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/user`,
            data: {
                'function': 'goToUserDetail',
                'memberID': $(this).attr('id')
            }
        }).done(function (data){
            if(data.data != null){
                window.location.href = data.data;
            }
        })
    })


})

function Reload(){
    let stt = 0
    for (let i in userData){
        stt = Number(Number(stt)+1)
        const html =   `<tr>
                            <td id="stt">${stt}</td>
                            <td>${userData[i]["fullname"]}</td>
                            <td id="name-email">${userData[i]["email"]}</td>
                            <td>${userData[i]["role"]["name"]}</td>
                            <td>
                                <a id="${userData[i]["id"]}" href="#" class="btn btn-sm btn-primary btn-edit">Sửa</a>
                                <a id="${userData[i]["id"]}" href="#" class="btn btn-sm btn-danger btn-delete">Xóa</a>
                                <a id="${userData[i]["id"]}" href="#" class="btn btn-sm btn-info btn-detail">Xem</a>
                            </td>
                        </tr>`
        $('#example').DataTable().row.add($(html)).draw()
    }

}

