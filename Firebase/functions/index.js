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

exports.publicCommunityNewMessageNotification = functions.https.onCall((data, context) => {

    //const EarthHemisphere = data.EarthHemisphere;

    /*var postTitle = newestPostJson['title'].rendered;
    var postSummary = newestPostJson['excerpt'].rendered.replace( /(<([^>]+)>)/ig, '');
    var postFeaturedImage = newestPostJson['jetpack_featured_media_url'];

    var message = {
        notification: {
            title: postTitle,
            body: postSummary
        },
        android: {
            notification: {
                image: '' + postFeaturedImage,
                color: '#' + postColor
            }
        },
        data: {
            title: postTitle,
            summary: postSummary,
            color: '#' + postColor,
            image: '' + postFeaturedImage
        },
        topic: 'NewestPosts'
    };

    admin.messaging().send(message)
        .then((response) => {

            res.status(200).send('Title: ' + postTitle + '<br/>' + 'Summary: ' + postSummary + '<br/>' + 'Color: ' + '#' + postColor + '<br/>' + 'Featured Image: ' + postFeaturedImage);

        })
        .catch((error) => {

            res.status(200).send('Error: ' + error);

        });*/

});