import finnhub


def main():
    import finnhub
    finnhub_client = finnhub.Client(api_key="c6rtv3iad3ifcngb77s0")
    print(finnhub_client.quote('AAPL'))


if __name__ == '__main__':
    main()