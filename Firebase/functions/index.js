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

    const notificationTopic = data.notificationTopic;
    const selfDisplayName = data.selfDisplayName;
    const selfUid = data.selfUid;
    const publicCommunityAction = data.publicCommunityAction;
    const nameOfCountry = data.nameOfCountry
    const vicinityLatitude = data.vicinityLatitude;
    const vicinityLongitude = data.vicinityLongitude;
    const publicCommunityName = data.publicCommunityName;
    const notificationLargeIcon = data.notificationLargeIcon;
    const messageContent = data.messageContent;

    var message = {

        android: {
            ttl: (3600 * 1000) * (1), // 1 Hour in Milliseconds

            priority: 'high',
        },

        data: {
            "selfDisplayName": selfDisplayName,
            "selfUid": selfUid,
            "publicCommunityAction": publicCommunityAction,
            "nameOfCountry": nameOfCountry,
            "vicinityLatitude": vicinityLatitude,
            "vicinityLongitude": vicinityLongitude,
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