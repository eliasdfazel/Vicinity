'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp({
    credential: admin.credential.applicationDefault(),
});


const XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;

const runtimeOptions = {
    timeoutSeconds: 313,
}

exports.publicCommunityNewMessageNotification = functions.runWith(runtimeOptions).https.onCall((data, context) => {

    const notificationTopic = data.notificationTopic
    const selfDisplayName = data.selfDisplayName
    const selfUid = data.selfUid
    const publicCommunityAction = data.publicCommunityAction
    const publicCommunityName = data.publicCommunityName
    const notificationLargeIcon = data.notificationLargeIcon
    const messageContent = data.messageContent

    var message = {
        notification: {
            title: selfDisplayName,
            body: messageContent
        },
        android: {
            ttl: (3600 * 1000) * (1), // 1 hour in milliseconds

            priority: 'high',
        },
        data: {
            "selfDisplayName": selfDisplayName,
            "selfUid": selfUid,
            "publicCommunityAction": publicCommunityAction,
            "publicCommunityName": publicCommunityName, 
            "notificationLargeIcon": notificationLargeIcon,
            "messageContent": messageContent
        },
        topic: notificationTopic
    };

    return admin.messaging().send(message).then((response) => {
        console.log('Successfully Sent ::: ', response);
    }).catch((error) => {
        console.log('Error Sending Message ::: ', error);
    });

});