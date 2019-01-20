var isSubmitting = false;
function runScript(docId,docName) {

    if(isSubmitting) {
        $.growl.error({ message: docName + " is already running." });
        return;
    }
    var data = {
        docId: docId
    };
    isSubmitting = true;

    $.growl({ title: "Test Case", message: docName + " have started running" });
    $.ajax({
        type: "POST",
        url: "/Selenium/rundoc",
        data: data,

        timeout: 100000,

        success: function (data) {
            isSubmitting = false;
            $.growl.notice({ message: docName + " have run successfully" });
        },
        error: function (e) {
            isSubmitting = false;
            //$.growl.error({ message: docName + " error: " + e });
        },
        done: function (e) {
            console.log("DONE");
        }
    });

    return false;
}

function deleteSuite(docId,docName) {

    var data = {
        docId: docId
    };

    $.ajax({
        type: "POST",
        url: "/Selenium/deletesuite",
        data: data,

        timeout: 100000,

        success: function (data) {
            //$.growl.notice({ message: docName + " have been deleted successfully" });
            //setTimeout(function(){
                location.href = "/Selenium/suite";
            //}, 2000);

        },
        error: function (e) {
            $.growl.error({ message: docName + " error: " + e });
        },
        done: function (e) {
            console.log("DONE");
        }
    });

}

function deleteScript(docId,docName) {

    var data = {
        docId: docId
    };

    $.ajax({
        type: "POST",
        url: "/Selenium/deletedoc",
        data: data,

        timeout: 100000,

        success: function (data) {
            //$.growl.notice({ message: docName + " have been deleted successfully" });
            //setTimeout(function(){
            location.href = "/Selenium/index";
            //}, 2000);

        },
        error: function (e) {
            $.growl.error({ message: docName + " error: " + e });
        },
        done: function (e) {
            console.log("DONE");
        }
    });

}



window.onload = function() {
    //document.getElementById("help").addEventListener("mouseover", mouseOver);
    $(".tip").mouseover(function() {
        $(".help").show();
    }).mouseout(function() {
        $(".help").hide();
    });
};


/*
function mouseOver() {
    console.log("adasdad");
    alert("...");
}*/


function errorResult(message) {
    if(message.length > 0){
        $.growl.warning({ message: message });
    }
}

function successAdd(message) {
    if(message.length > 0){
        $.growl.notice({ message: message });
    }
}
