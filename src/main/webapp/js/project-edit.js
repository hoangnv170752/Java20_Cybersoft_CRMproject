$(document).ready(function () {
    let projectId = 0
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/project-edit`,
    }).done(function (data) {
        if (data[0].data != null) {
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }
        if(data[1].data != null){
            let projectInfo = data[1].data
            console.log(projectInfo)
            projectId = projectInfo["id"]
            $('#input-name').attr('value',projectInfo["name"])
            $('#input-start-date').attr('value',projectInfo["start_date"])
            $('#input-end-date').attr('value',projectInfo["end_date"])
            if(data[2].data != null){
                let leaderList = data[2].data
                for (const i in leaderList){
                    const html = `<option value="${leaderList[i]["id"]}">${leaderList[i]["fullname"]}</option>`
                    $('#select-leader').append(html)
                }
                document.getElementById('select-leader').value = projectInfo["leader"]["id"]
            }
            if(!data[3].data){
                $('#select-leader').attr('disabled',true)
            }
        } else{
            $('#tittle-content').html('Không tìm thấy dữ liệu dự án')
        }
    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/project-edit`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    $('#btn-edit-project').click(function (e){
        e.preventDefault()
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/project-edit`,
            data: {
                'function': 'editProject',
                'id': projectId,
                'name': $('#input-name').val(),
                'leaderId': document.getElementById('select-leader').value,
                'start-date': $('#input-start-date').val(),
                'end-date': $('#input-end-date').val()
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