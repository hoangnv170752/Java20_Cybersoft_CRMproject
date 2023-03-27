let taskData;
$(document).ready(function (){
    const thisList = $('#task-list')
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/task`,
    }).done(function (data){
        if(data[0].data != null){
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src','plugins/images/users/'+data[0].data["avatar"])
        }
        if(data[1].data != null){
            taskData = data[1].data
            //console.log(taskData)
            for (const i in taskData){
                let stt = Number(Number(i)+1)
                const html =   `<tr>
                                    <td id="stt">${stt}</td>
                                    <td id="task-name" >${taskData[i]["name"]}</td>
                                    <td id="project-name" >${taskData[i]["project"]["name"]}</td>
                                    <td>${taskData[i]["user"]["fullname"]}</td>
                                    <td>${taskData[i]["start_date"]}</td>
                                    <td>${taskData[i]["end_date"]}</td>
                                    <td>${taskData[i]["status"]["name"]}</td>
                                    <td>
                                        <a id="${taskData[i]["id"]}" href="#" class="btn btn-sm btn-primary btn-edit">Sửa</a>
                                        <a id="${taskData[i]["id"]}" href="#" class="btn btn-sm btn-danger btn-delete">Xóa</a>
                                    </td>
                                </tr>`
                //$('#example').find('tbody').append(html)
                thisList.append(html)
            }
            $('#example').DataTable()
        } else {
            $('#example').DataTable()
        }

    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/task`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    $('#add-task').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/task`,
            data: {
                'function': 'goToAddTask'
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
        const task = This.closest('tr').find("td[id='task-name']").html()
        const project = This.closest('tr').find("td[id='project-name']").html()
        let ok = confirm("Bạn có muốn xóa công việc có tên: "+task+" của "+project+" không?")
        if(ok){
            $.ajax({
                method: 'POST',
                url: `http://localhost:8080/api/task`,
                data: {
                    'function': 'deleteTask',
                    'taskID': $(this).attr('id')
                }
            }).done(function (data){
                if(data.data == 1){
                    let index = parseInt(This.closest('tr').find("td[id='stt']").html())-1
                    taskData.splice(index,1)
                    This.closest('tr').find("td[id='stt']").html()
                    $('#example').DataTable().clear().draw()
                    Reload()
                    alert(data.message)
                } else if(data.data == 0){
                    alert(data.message)
                } else if(data.data == -1){
                    alert(data.message)
                }
            })
        }
    })

    thisList.on('click','.btn-edit',function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/task`,
            data: {
                'function': 'goToEditTask',
                'taskID': $(this).attr('id')
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
    for (let i in taskData){
        stt = Number(Number(stt)+1)
        const html =   `<tr>
                            <td id="stt">${stt}</td>
                            <td id="task-name" >${taskData[i]["name"]}</td>
                            <td id="project-name" >${taskData[i]["project"]["name"]}</td>
                            <td>${taskData[i]["user"]["fullname"]}</td>
                            <td>${taskData[i]["start_date"]}</td>
                            <td>${taskData[i]["end_date"]}</td>
                            <td>${taskData[i]["status"]["name"]}</td>
                            <td>
                                <a id="${taskData[i]["id"]}" href="#" class="btn btn-sm btn-primary btn-edit">Sửa</a>
                                <a id="${taskData[i]["id"]}" href="#" class="btn btn-sm btn-danger btn-delete">Xóa</a>
                            </td>
                        </tr>`
        $('#example').DataTable().row.add($(html)).draw()
    }

}

