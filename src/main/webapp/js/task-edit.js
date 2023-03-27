$(document).ready(function () {
    let taskId = 0
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/task-edit`,
    }).done(function (data) {
        if (data[0].data != null) {
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }
        if(data[1].data != null){
            let taskInfo = data[1].data
            taskId = taskInfo["id"]
            $('#input-task-name').attr('value',taskInfo["name"])
            $('#input-start-date').attr('value',taskInfo["start_date"])
            $('#input-end-date').attr('value',taskInfo["end_date"])

            if(data[2].data != null){
                let projectList = data[2].data
                for (const i in projectList){
                    const html = `<option value="${projectList[i]["id"]}">${projectList[i]["name"]}</option>`
                    $('#select-project').append(html)
                }
                document.getElementById('select-project').value = taskInfo["project"]["id"]
            }
            if(data[3].data != null){
                let memberList = data[3].data
                for (const i in memberList){
                    const html = `<option value="${memberList[i]["id"]}">${memberList[i]["fullname"]}</option>`
                    $('#select-member').append(html)
                }
                document.getElementById('select-member').value = taskInfo["user"]["id"]
            }
            if(data[4].data != null){
                let statusList = data[4].data
                for (const i in statusList){
                    const html = `<option value="${statusList[i]["id"]}">${statusList[i]["name"]}</option>`
                    $('#select-status').append(html)
                }
                document.getElementById('select-status').value = taskInfo["status"]["id"]
            }
        } else {
            $('#title-content').html('Không tìm thấy dữ liệu công việc')
        }

    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/task-edit`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    $('#btn-edit-task').click(function (e){
        e.preventDefault()
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/task-edit`,
            data: {
                'function': 'editTask',
                'id': taskId,
                'projectID': document.getElementById('select-project').value,
                'taskName': $('#input-task-name').val(),
                'memberID': document.getElementById('select-member').value,
                'start-date': $('#input-start-date').val(),
                'end-date': $('#input-end-date').val(),
                'statusID': document.getElementById('select-status').value
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