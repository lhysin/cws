import json
import logging
import os
import boto3
import time

# CloudWatch Logs를 사용하기 위한 클라이언트 생성
client = boto3.client('logs')

# 로깅 설정
logger = logging.getLogger()
logger.setLevel(logging.INFO)

# error_messages.json 파일을 불러와 error_messages 변수에 저장
with open("error_messages.json") as f:
    error_messages = json.load(f)

def lambda_handler(event, context):
    # 로그 정보 출력
    logger.info('Received event: %s' % json.dumps(event))
    
    try:
        data = json.loads(json.dumps(event))
    except ValueError:
        logging.info(f'body info : {event}')
        return {
            "statusCode": 400,
            "body": json.dumps({"error": error_messages["invalid_request"]})
        }
    except json.JSONDecodeError as e:
        logging.error(f'JSONDecodeError info : {str(e)}')
        error_message = {'error': 'Invalid JSON format: ' + str(e)}
        response = {'statusCode': 400, 'body': json.dumps(error_message)}
        return response
    
    # data에서 필요한 값을 추출하고, 값이 빠졌을 경우 예외 처리
    try:
        itemCode = data["itemCode"]
        option = data["option"]
    except KeyError as e:
        logging.error(f'KeyError info : {str(e)}')
        return {
            "statusCode": 400,
            "body": json.dumps({"error": error_messages["missing_parameter"].format(e)})
        }

    # 값이 잘못되었을 경우 예외 처리
    if not isinstance(itemCode, str):
        return {
            "statusCode": 400,
            "body": json.dumps({"error": error_messages["invalid_parameter"].format("itemCode")})
        }
    

    # CloudWatch Logs 그룹과 스트림 생성
    log_group_name = '/aws/lambda/%s' % os.environ['AWS_LAMBDA_FUNCTION_NAME']
    log_stream_name = context.aws_request_id

    # CloudWatch Logs에 로그 전송
    try:
        response = client.create_log_stream(
            logGroupName=log_group_name,
            logStreamName=log_stream_name
        )
        response = client.put_log_events(
            logGroupName=log_group_name,
            logStreamName=log_stream_name,
            logEvents=[{
                'timestamp': int(time.time() * 1000),
                'message': json.dumps({'event': event})
            }]
        )
        print(response)
    except Exception as e:
        print("Failed to put log events:", e)

    # 처리 로직 구현
    #
    return {
        "statusCode": 200,
        "body": json.dumps({"result": "success"})
    }