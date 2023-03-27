$(document).ready(function () {
    let taskId = 0
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/profile-task-update`,
    }).done(function (data){
        if(data[0].data != null){
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src','plugins/images/users/'+data[0].data["avatar"])
        }
        if(data[1].data != null){
            let taskInfo = data[1].data
            taskId = taskInfo["id"]
            $('#project-name').attr('value',taskInfo["project"]["name"])
            $('#task-name').attr('value',taskInfo["name"])
            $('#task-start-date').attr('value',taskInfo["start_date"])
            $('#task-end-date').attr('value',taskInfo["end_date"])
            if(data[2].data != null){
                let statusList = data[2].data
                for (const i in statusList){
                    const html = `<option value="${statusList[i]["id"]}">${statusList[i]["name"]}</option>`
                    $('#select-task').append(html)
                }
                document.getElementById('select-task').value = taskInfo["status"]["id"]
            }
        } else {
            $('#tittle-content').html('Không tìm thấy dữ liệu công việc')
        }
    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/profile-task-update`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    // $('#task-status').change(function (){
    //     $.ajax({
    //         method: 'POST',
    //         url: `http://localhost:8080/api/profile-taskstatus-update`,
    //         data: {
    //             'taskStatus': $('#task-status').val()
    //         }
    //     })
    // })

    $('#btn-save-status').click(function (e){
        e.preventDefault()
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/profile-task-update`,
            data: {
                'function': 'saveStatus',
                'taskId': taskId,
                'statusId': $('#select-task').val()
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