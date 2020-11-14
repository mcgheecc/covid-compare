import { Component, OnInit } from '@angular/core';
import { AdminService} from '../../service/admin.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(protected adminService: AdminService) {

  }

  ngOnInit(): void {
  }

  showAdmin() {
    this.adminService.showAdmin();
  }

  showAbout() {

  }
}
