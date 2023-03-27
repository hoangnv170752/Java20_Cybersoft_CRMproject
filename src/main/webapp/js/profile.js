$(document).ready(function (){
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/profile`,
    }).done(function (data){
        if(data[0].data != null){
            //console.log(data.data)
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src','plugins/images/users/'+data[0].data["avatar"])
            $('#user-name').html(data[0].data["fullname"])
            $('#user-email').html(data[0].data["email"])
            $('#user-avatar').attr('src','plugins/images/users/'+data[0].data["avatar"])
        }
        if(data[1].data != null){
            $('#task-unbegun').html(data[1].data[0]+'%')
            $('#task-doing').html(data[1].data[1]+'%')
            $('#task-finish').html(data[1].data[2]+'%')
            $('#progress-bar-unbegun').attr('style','width: '+data[1].data[0]+'%')
            $('#progress-bar-doing').attr('style','width: '+data[1].data[1]+'%')
            $('#progress-bar-finish').attr('style','width: '+data[1].data[2]+'%')
        }
        if(data[2].data != null){
            //console.log(data[2].data)
            for (const i in data[2].data){
                let stt = Number(Number(i)+1)
                const html = `<tr>
                                    <td>${stt}</td>
                                    <td>${data[2].data[i]["name"]}</td>
                                    <td>${data[2].data[i]["project"]["name"]}</td>
                                    <td>${data[2].data[i]["start_date"]}</td>
                                    <td>${data[2].data[i]["end_date"]}</td>
                                    <td>${data[2].data[i]["status"]["name"]}</td>
                                    <td>
                                        <a class="btn btn-sm btn-primary btn-update" 
                                        id="${data[2].data[i]["id"]}"  >Cập nhật trạng thái</a>
                                    </td>
                               </tr>`
                //$('#example').find('tbody').append(html)
                $('#task-list').append(html)
            }
        }
        $('#example').DataTable()
    })
//<a href="/login?id=${data[2].data[i]["id"]}"
    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/profile`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })

    $('#task-list').on('click','.btn-update',function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/profile`,
            data: {
                'function': 'goToTaskUpdate',
                'taskID': $(this).attr('id')
            }
        }).done(function (data){
            if(data.data != null){
                window.location.href = data.data;
            }
        })

    })
})