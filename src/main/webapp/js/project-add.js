$(document).ready(function () {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/project-add`,
    }).done(function (data) {
        if (data[0].data != null) {
            //console.log(data.data)
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }
        if(data[1].data != null){
            let leaderList = data[1].data
            for (const i in leaderList){
                const html = `<option value="${leaderList[i]["id"]}">${leaderList[i]["fullname"]}</option>`
                $('#select-leader').append(html)
            }
        }else{
            $('#select-leader').append(`<option>N/A</option>`)
        }
    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/project-add`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    $('#btn-add-project').click(function (e){
        e.preventDefault()
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/project-add`,
            data: {
                'function': 'addProject',
                'name': $('#input-name').val(),
                'leaderId': document.getElementById('select-leader').value,
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