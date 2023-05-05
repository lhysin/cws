import { Loki } from "@loki/core";
import { HttpTransport } from "@loki/transport-http";
import AWS from "aws-sdk";
import serverless from "serverless-http";

const loki = new Loki({
    transports: [
        new HttpTransport({
            endpoint: "http://144.24.91.88:3100/loki/api/v1/push",
        }),
    ],
});

const sqs = new AWS.SQS();
const queueUrl = process.env.QUEUE_URL;

const handler = async (event) => {
    console.log(`Received message: ${JSON.stringify(event)}`);

    for (const record of event.Records) {
        try {
            await loki.push({
                stream: {
                    label: "sqs",
                },
                values: [
                    [Date.now(), JSON.stringify(record)],
                ],
            });
        } catch (error) {
            console.error(`Error sending log to Loki: ${error}`);
        }
    }
};

export const itemConsumer = serverless(handler);