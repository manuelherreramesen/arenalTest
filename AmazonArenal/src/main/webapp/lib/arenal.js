$(document).ready(function () {
    $('#productTable tbody').loading();

    $.ajax({
        url: "/listBuckets"
    }).then(function (data) {
        loadBuckets(data)
    });
});


function loadBuckets(data) {
    if ($("#productTable tbody").length == 0) {
        $("#productTable").append("<tbody></tbody>");
    }
    $('#productTable tbody').html("");
    if (data.length > 0) {
        data.map(data => {

            var creationDate = new Date(data.creationDate);
            $("#productTable tbody").append(
                `<tr>
                    <td> <a href="#" onclick="loadObjects('${data.name}')"> ${data.name}</a></td> +
                    <td> ${data.owner.displayName} </td>
                    <td> ${creationDate.toString()} </td>
                    </tr>`
            );
        });
    } else {
        $("#productTable tbody").append(
            `<tr>
                <td colspan="3"> No Data</td>
                </tr>`
        );
    }

    $('#productTable tbody').loading('stop');
};


function loadObjects(bucketName) {
    $('#objectTable tbody').loading();
    $.ajax({
        url: "/bucket/" + bucketName
    }).then(function (data) {
        if ($("#objectTable tbody").length == 0) {
            $("#objectTable").append("<tbody></tbody>");
        }

        $('#objectTable tbody').html("");

        if (data.length > 0) {
            data.map(data => {

                var creationDate = new Date(data.lastModified);
                $("#objectTable tbody").append(
                    `<tr>
                <td> <a data-toggle="modal" data-target="#objectModal" href='#' onclick="loadModalObject('${data.bucketName}','${data.key}')"> ${data.key}</a></td> +
                <td> ${data.bucketName} </td>
                <td> ${creationDate.toString()} </td>
                </tr>`
                );
            });
        } else {
            $("#objectTable tbody").append(
                `<tr>
                <td colspan="3"> No Data</td>
                </tr>`
            );
        }

        $('#objectTable tbody').loading('stop');
    });
};

function loadModalObject(bucketName, key) {
    $('#objectModal').loading();
    $.ajax({
        url: `/object/${bucketName}/${key}`
    }).then(function (data) {
            const modalBody = $("#objectModal .modal-body");
            modalBody.html("");

            modalBody.append(
                `<h3>${key}</h3>`);
            if (data.indexOf(".png") > -1) {
                modalBody.append(`<img src="${data}" height="300" width="500"/>`);
            } else {
                modalBody.append(`
                        <video width="500" height="300" controls>
                          <source src="${data}" type="video/mp4">
                          <source src="${data}" type="video/ogg">
                        Your browser does not support the video tag.
                        </video>`);
            }
        }
    ).always(() => {
        $('#objectModal').loading('stop');
    });
}


