$(document).ready(function () {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/user-detail`,
    }).done(function (data) {
        if(data[0].data != null){
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src','plugins/images/users/'+data[0].data["avatar"])
        }
        if(data[1].data != null){
            $('#member-name').html(data[1].data["fullname"])
            $('#member-email').html(data[1].data["email"])
            $('#member-avatar').attr('src','plugins/images/users/'+data[1].data["avatar"])
        } else {
            $('#page-title').html("Không tìm thấy dữ liệu thành viên")
        }
        if(data[2].data != null){
            $('#task-unbegun').html(data[2].data[0]+'%')
            $('#task-doing').html(data[2].data[1]+'%')
            $('#task-finish').html(data[2].data[2]+'%')
            $('#progress-bar-unbegun').attr('style','width: '+data[2].data[0]+'%')
            $('#progress-bar-doing').attr('style','width: '+data[2].data[1]+'%')
            $('#progress-bar-finish').attr('style','width: '+data[2].data[2]+'%')
        }
        if(data[3].data != null){
            for (const i in data[3].data){
                const html =   `<a href="#">
                                    <div class="mail-contnet">
                                        <h4>${data[3].data[i]["project"]["name"]}</h4>
                                        <h5>${data[3].data[i]["name"]}</h5>
                                        <span class="mail-desc"></span>
                                        <span class="time">Bắt đầu: ${data[3].data[i]["start_date"]}</span>
                                        <span class="time">Kết thúc: ${data[3].data[i]["end_date"]}</span>
                                    </div>
                                </a>`
                if(data[3].data[i]["status"]["id"] == 1){
                    $('#list-unbegun').append(html)
                } else if(data[3].data[i]["status"]["id"] == 2){
                    $('#list-doing').append(html)
                } else if (data[3].data[i]["status"]["id"] == 3){
                    $('#list-finish').append(html)
                }
            }
        }
    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/user-detail`,
            data: {
                'function': 'logout'
            }
        }).done(function (data){
            if(data.data){
                window.location.href = '/login'
            }
        })
    })
})
