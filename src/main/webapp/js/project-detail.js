$(document).ready(function () {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/project-detail`,
    }).done(function (data) {
        if(data[0].data != null){
            $('#user-name-bar').html(data[0].data["fullname"])
            $('#user-avatar-bar').attr('src','plugins/images/users/'+data[0].data["avatar"])
        }
        if(data[1].data != null){
            $('#page-title').html("Chi tiết dự án:  "+data[1].data["name"])
            $('#title-name-leader').html("Quản lý:  "+data[1].data["leader"]["fullname"])
            $('#title-start-date').html("Ngày bắt đầu:  "+data[1].data["start_date"])
            $('#title-end-date').html("Ngày kết thúc:  "+data[1].data["end_date"])
        } else {
            $('#page-title').html("Không tìm thấy dữ liệu dự án")
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
            //$('#page-title').html("Chi tiết "+data[3].data)
            for (const i in data[3].data){
                const This = $('#task-detail')
                let user = data[3].data[i]["user"]
                let taskList = data[3].data[i]["taskList"]
                const html =   `<div class="col-xs-12">
                                    <a href="#" class="group-title">
                                        <img src="plugins/images/users/${user["avatar"]}" width="30" class="img-circle" />
                                        <span>${user["fullname"]}</span>
                                    </a>
                                </div>
                                <div class="col-md-4">
                                    <div id="" class="white-box">
                                        <h3 class="box-title">Chưa thực hiện</h3>
                                        <div id="list-unbegun${i}" class="message-center">
                                            
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="white-box">
                                        <h3 class="box-title">Đang thực hiện</h3>
                                        <div id="list-doing${i}" class="message-center">
                                            
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="white-box">
                                        <h3 class="box-title">Đã hoàn thành</h3>
                                        <div id="list-finish${i}" class="message-center">
                                            
                                        </div>
                                    </div>
                                </div>`
                This.append(html)
                for (const k in taskList){
                    const html1 =  `<a href="#" xmlns="http://www.w3.org/1999/html">
                                        <div class="mail-contnet">
                                            <h5>${taskList[k]["name"]}</h5>
                                            <span class="mail-desc"></span>
                                            <span class="time">Bắt đầu: ${taskList[k]["start_date"]}</span>
                                            <span class="time">Kết thúc: ${taskList[k]["end_date"]}</span>
                                        </div>
                                    </a>`
                    if (taskList[k]["status"]["id"] == 1) {
                        This.find("div[id='list-unbegun"+i+"']").append(html1)
                    } else if (taskList[k]["status"]["id"] == 2) {
                        This.find("div[id='list-doing"+i+"']").append(html1)
                    } else if (taskList[k]["status"]["id"] == 3) {
                        This.find("div[id='list-finish"+i+"']").append(html1)
                    }
                }
            }
        }
    })

    $('#logout').click(function (){
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/project-detail`,
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
