import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogContent, MatDialogActions, MatDialogClose } from '@angular/material/dialog';
import { SimulatedLoan } from '../loans.service';
import { CommonModule } from '@angular/common';
import { MatAnchor } from "@angular/material/button";
import { MatDivider } from "@angular/material/divider";

@Component({
  selector: 'app-simulate-loan-dialog',
  imports: [MatDialogContent, CommonModule, MatDialogActions, MatDialogClose, MatAnchor, MatDivider],
  templateUrl: './simulate-loan-dialog.html',
  styleUrl: './simulate-loan-dialog.scss'
})
export class SimulateLoanDialog {
  public data = inject(MAT_DIALOG_DATA) as SimulatedLoan;
}
