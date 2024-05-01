# Currency Converter App

Welcome to the Currency Converter App! This application allows users to convert currency values between different countries using the latest exchange rates provided by the Open Exchange Rates API.

## Features

1. **Country Selection**: Users can select both the source and target countries from a list of available countries. A search option is also provided to facilitate finding specific countries quickly.

2. **Currency Conversion**: After selecting both source and target countries, users can enter the value they want to convert. The app then calculates the exchange rate and displays the converted value.

3. **API Usage Information**: Users can view the usage information of the Open Exchange Rates API, including the remaining number of requests allowed for their account.

## Architecture

The app is built using the MVVM (Model-View-ViewModel) architecture pattern. This architecture separates the UI (View) from the business logic (ViewModel) and the data (Model), resulting in a clean and maintainable codebase.

## Technologies Used

- **Java**: The app is developed using the Java programming language.
- **Open Exchange Rates API**: Exchange rate data is obtained from the Open Exchange Rates API.
- **MVVM Architecture**: The app follows the MVVM architecture pattern for separation of concerns and better code organization.

## Getting Started

To get started with the Currency Converter App, follow these steps:

1. **Clone the Repository**: Clone this repository to your local machine using Git:

    ```
    git clone https://github.com/gnyanateja/currency-converter-app.git
    ```

2. **API Key**: Sign up for an account on the Open Exchange Rates website to obtain an API key. Once you have the API key, replace the API_KEY in your app's `build.gradle` with your actual API key.

3. **Build and Run**: Build and run the app using your preferred IDE or build tool.

## Contributing

Contributions are welcome! If you have any suggestions, feature requests, or bug reports, please open an issue on the GitHub repository.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
