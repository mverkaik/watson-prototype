var PIZZA_DIALOG_ID = '4af04133-e31b-4be4-8b54-24c207a455e2';

/** Dialog id */
var RUN_DIALOG_ID = 'ae0dac85-c1ce-4d27-8b1d-39c3c0f7e901';

/** Dialog API root URI */
var API_ROOT = '/api/dialogs/id/' + RUN_DIALOG_ID;

/** Dialog container */
var dialogContainer = $('#dialog-container');

/** Dialog input field */
var inputField = $('#human-input');

/** Races container (search results) */
var racesContainer = $('#races-container');

/** Keep state of dialog */
var dialog;

/** Keep state of profile */
var profile;


/**
 *  Send input back to server
 */
var converse = function () {

    var inputVal = inputField[0].value.trim();
    console.log('converse with watson: ' + inputVal);
    if (inputVal.length > 0) {
        dialogContainer.append('<p class="human-msg">' + inputVal + '</p>');
        $.post(API_ROOT + '/conversation',
            'conversationID=' + dialog.conversation_id +
            '&clientID=' + dialog.client_id +
            '&input=' + inputVal)
            .done(function (response) {
                var callback;
                dialog = response;
                for (var i = 0; i < dialog.response.length; i++) {
                    var watsonMsg = dialog.response[i];
                    if (watsonMsg == 'CMD:findRaces') {
                        callback = findRaces;
                    }
                    else {
                        callback = null;
                        dialogContainer.append('<p class="watson-msg">' + watsonMsg + '</p>');
                    }
                }
                getProfile(callback);
                console.log('conversed successfully');
            })
            .fail(function () {
                console.log('failed to converse');
            });
        inputField[0].value = '';
    }
    inputField.focus();
};


/**
 *  Start a conversation
 */
var startConversation = function () {

    $.get(API_ROOT + '/conversation/create')
        .done(function (response) {
            dialog = response;
            dialogContainer.append('<p class="watson-msg">' + dialog.response[0] + '</p>');
            $('#conversation-id').html('<b>conversation_id: </b>' + dialog.conversation_id);
            $('#client-id').html('<b>client_id: </b>' + dialog.client_id);
            inputField.focus();
            console.log('started conversation');
        })
        .fail(function () {
            console.log('failed to start a conversation');
        });
};


/**
 * Get profile values
 */
var getProfile = function (callback) {

    console.log('get profile:');
    var url = API_ROOT + '/profile/' + dialog.client_id;
    $.get(url)
        .done(function (response) {
            profile = response;
            $('#user-name').html('<b>userName: </b>' + profile.userName);
            $('#zip').html('<b>zip: </b>' + profile.zip);
            $('#race-type').html('<b>raceType: </b>' + profile.raceType);
            console.log('got profile:');
            console.log(profile);
            if (typeof callback === 'function') {
                console.log('execute callback');
                callback();
            }
        })
        .fail(function (response) {
            console.log('failed to get profile values');
            console.log(response);
        });
};


/**
 * Find races based on user's input
 */
var findRaces = function () {

    console.log('find races');
    var url = '/api/races?type=' + profile.raceType + '&zip=' + profile.zip;
    $.get(url)
        .done(function (response) {
            for (var i in response) {
                racesContainer.append('<div>' +
                    '<p class="race-name"><a href="' +  response[i].link + '">' + response[i].name + '</a></p>' +
                    '<p class="race-location">' + response[i].location + '</p>' +
                    '<p class="race-date">' + response[i].date + '</p>' +
                    '</div>');
            }
            console.log('done');
        })
        .fail(function (response) {
            console.log('fail');
            console.log(response);
        });
};


var getRaceInfo = function () {

    $.get('/api/races/race')
        .done(function (response) {
            // console.log('got race info:');
            // console.log(response);
            $('#race-container').html(response);
        });
};


/**
 * Init when document is loaded
 */
$(function () {

    // send button handler
    $('#send-btn').click(converse);

    // enter key in input field handler
    inputField.keypress(function (e) {
        if (e.keyCode == 13) {
            converse();
        }
    });

    // start conversation
    startConversation();

    $('#find-races-btn').click(function () {
        findRaces();
    });

    getRaceInfo();
});