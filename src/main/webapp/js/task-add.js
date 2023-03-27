$(document).ready(function () {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/task-add`,
    }).done(function (data) {
        if (data[0].data != null) {
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }
        if(data[1].data != null){
            let projectList = data[1].data
            for (const i in projectList){
                const html = `<option value="${projectList[i]["id"]}">${projectList[i]["name"]}</option>`
                $('#select-project').append(html)
            }
        } else{
            $('#select-role').append(`<option>N/A</option>`)
        }
        if(data[2].data != null){
            let memberList = data[2].data
            for (const i in memberList){
                const html = `<option value="${memberList[i]["id"]}">${memberList[i]["fullname"]}</option>`
                $('#select-member').append(html)
            }
        } else{
            $('#select-role').append(`<option>N/A</option>`)
        }

    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/task-add`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    $('#btn-add-task').click(function (e){
        e.preventDefault()
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/task-add`,
            data: {
                'function': 'addTask',
                'projectID': document.getElementById('select-project').value,
                'taskName': $('#input-task-name').val(),
                'memberID': document.getElementById('select-member').value,
                'start-date': $('#input-start-date').val(),
                'end-date': $('#input-end-date').val()
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