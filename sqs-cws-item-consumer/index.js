const AWS = require('aws-sdk');
const winston = require('winston');
const LokiTransport = require('winston-loki');

const sqs = new AWS.SQS({ region: 'ap-northeast-2' });

const logger = winston.createLogger({
    transports: [
        new LokiTransport({
            host: 'http://144.24.91.88',
            port: 3100,
            labels: { app: 'sqs-cws-item-consumer' },
            format: winston.format.combine(
                winston.format.timestamp(),
                winston.format.json()
            )
        })
    ]
});

exports.handler = async (event, context) => {
    logger.info('start sqs-cws-item-consumer');

    try {
        const messages = await sqs.receiveMessage({
            QueueUrl: 'https://sqs.ap-northeast-2.amazonaws.com/627500151784/std-dev-sqs-cws-item',
            MaxNumberOfMessages: 10
        }).promise();

        if (messages && messages.Messages && messages.Messages.length > 0) {
            for (const message of messages.Messages) {
                // process message here
                logger.info(`Received message: ${message.Body}`);

            }
        }
    } catch (err) {
        logger.error(err.message);
    }

    logger.info('end sqs-cws-item-consumer');
};