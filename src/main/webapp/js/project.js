let projectData;
$(document).ready(function (){
    const thisList = $('#project-list')
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/project`,
    }).done(function (data){
        if(data[0].data != null){
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src','plugins/images/users/'+data[0].data["avatar"])
        }
        if(data[1].data != null){
            projectData = data[1].data
            for (const i in projectData){
                let stt = Number(Number(i)+1)
                const html =   `<tr>
                                    <td id="stt">${stt}</td>
                                    <td id="name-project" >${projectData[i]["name"]}</td>
                                    <td>${projectData[i]["start_date"]}</td>
                                    <td>${projectData[i]["end_date"]}</td>
                                    <td>${projectData[i]["leader"]["fullname"]}</td>
                                    <td>
                                        <a id="${projectData[i]["id"]}" href="#" class="btn btn-sm btn-primary btn-edit">Sửa</a>
                                        <a id="${projectData[i]["id"]}" href="#" class="btn btn-sm btn-danger btn-delete">Xóa</a>
                                        <a id="${projectData[i]["id"]}" href="#" class="btn btn-sm btn-info btn-detail">Xem</a>
                                    </td>
                                </tr>`
                //$('#example').find('tbody').append(html)
                $('#project-list').append(html)
            }
            $('#example').DataTable()
        } else {
            $('#example').DataTable()
        }

    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/project`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    $('#add-project').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/project`,
            data: {
                'function': 'goToAddProject'
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
        const This = $(this)
        const project = This.closest('tr').find("td[id='name-project']").html()
        let ok = confirm("Bạn có muốn xóa dự án có tên: "+project+" này không?")
        if(ok){
            $.ajax({
                method: 'POST',
                url: `http://localhost:8080/api/project`,
                data: {
                    'function': 'deleteProject',
                    'projectID': $(this).attr('id')
                }
            }).done(function (data){
                if(data.data == 1){
                    let index = parseInt(This.closest('tr').find("td[id='stt']").html())-1
                    projectData.splice(index,1)
                    This.closest('tr').find("td[id='stt']").html()
                    $('#example').DataTable().clear().draw()
                    Reload()
                    alert(data.message)
                } else if(data.data == 0){
                    alert(data.message)
                } else if(data.data == -1){
                    alert('Tồn tại công việc trong dự án này. Vui lòng chuyển công việc sang dự án khác hoặc xóa công việc trước khi xóa dự án')
                }
            })
        }
    })

    thisList.on('click','.btn-edit',function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/project`,
            data: {
                'function': 'goToEditProject',
                'projectID': $(this).attr('id')
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
            url: `http://localhost:8080/api/project`,
            data: {
                'function': 'goToProjectDetail',
                'projectID': $(this).attr('id')
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
    for (let i in projectData){
        stt = Number(Number(stt)+1)
        const html =   `<tr>
                            <td id="stt">${stt}</td>
                            <td id="name-project" >${projectData[i]["name"]}</td>
                            <td>${projectData[i]["start_date"]}</td>
                            <td>${projectData[i]["end_date"]}</td>
                            <td>${projectData[i]["leader"]["fullname"]}</td>
                            <td>
                                <a id="${projectData[i]["id"]}" href="#" class="btn btn-sm btn-primary btn-edit">Sửa</a>
                                <a id="${projectData[i]["id"]}" href="#" class="btn btn-sm btn-danger btn-delete">Xóa</a>
                                <a id="${projectData[i]["id"]}" href="#" class="btn btn-sm btn-info btn-detail">Xem</a>
                            </td>
                        </tr>`
        $('#example').DataTable().row.add($(html)).draw()
    }

}

