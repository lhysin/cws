import serverless from "serverless-http";

const handler = async (event) => {
    console.log(`Received message: ${JSON.stringify(event)}`);
};

export const itemConsumer = serverless(handler);