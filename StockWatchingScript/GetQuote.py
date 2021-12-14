import json
import sys
import finnhub


def main():
    client = finnhub.Client(api_key=sys.argv[1])
    output = json.dumps(client.quote(sys.argv[2]))
    output = output.replace("{", "")
    output = output.replace("}", "")
    output = output.replace('"', "")
    output = output.replace('"', "")
    output = output.replace('"', "")
    output = output.replace(' ', "")
    print(output)


if __name__ == '__main__':
    main()