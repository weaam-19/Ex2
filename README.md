# Ex2 Spreadsheet Project

## Overview
This project is a fully functional spreadsheet application that allows users to:
- Input text, numbers, or formulas into cells.
- Dynamically evaluate cell values with support for dependencies between cells.
- Handle errors such as invalid formulas (`ERR_FORM`) and circular references (`ERR_CYCLE`).

## Features
- **Dynamic Evaluation:** Supports numeric inputs (e.g., `123`) and formulas (e.g., `=A1+B2`).
- **Error Handling:**
  - `ERR_FORM` for invalid formulas.
  - `ERR_CYCLE` for circular dependencies.
  - Division by zero detection.
- **Graphical User Interface (GUI):** Intuitive interface for interacting with the spreadsheet.
- **Saving and Loading:** Save the spreadsheet state to a file and reload it.

## How to Run

1. **Clone the Repository:**
   ```bash
   git clone <repository-url>
   cd <project-folder>
